package com.example.torneos.service;

import com.example.torneos.entity.Bracket;
import com.example.torneos.entity.Enfrentamiento;
import com.example.torneos.entity.Ronda;
import com.example.torneos.exception.ResourceNotFoundException;
import com.example.torneos.repository.BracketRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ExportacionBracketService {

    private final BracketRepository bracketRepository;

    @Transactional(readOnly = true)
    public byte[] exportarBracketAPDF(Long bracketId) throws IOException {
        Bracket bracket = bracketRepository.findById(bracketId)
                .orElseThrow(() -> new ResourceNotFoundException("Bracket no encontrado con ID: " + bracketId));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Título
        Paragraph titulo = new Paragraph("Bracket del Torneo: " + bracket.getTorneo().getNombre())
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(titulo);

        // Información del torneo
        Paragraph info = new Paragraph()
                .add("Juego: ").add(bracket.getTorneo().getJuego()).add("\n")
                .add("Formato: ").add(bracket.getTorneo().getFormato().toString()).add("\n")
                .add("Estado: ").add(bracket.getEstado().toString()).add("\n")
                .add("Fecha de Generación: ").add(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        document.add(info);

        document.add(new Paragraph("\n"));

        // Rondas y enfrentamientos
        for (Ronda ronda : bracket.getRondas()) {
            Paragraph rondaTitulo = new Paragraph(ronda.getNombre())
                    .setFontSize(14)
                    .setBold();
            document.add(rondaTitulo);

            // Crear tabla para los enfrentamientos
            Table table = new Table(new float[]{2, 2, 1, 2, 2});
            table.addHeaderCell("Participante 1");
            table.addHeaderCell("Participante 2");
            table.addHeaderCell("Resultado");
            table.addHeaderCell("Score");
            table.addHeaderCell("Ganador");

            for (Enfrentamiento enfrentamiento : ronda.getEnfrentamientos()) {
                String p1 = enfrentamiento.getParticipante1() != null ? enfrentamiento.getParticipante1().getNombre() : "TBD";
                String p2 = enfrentamiento.getParticipante2() != null ? enfrentamiento.getParticipante2().getNombre() : "TBD";
                String estado = enfrentamiento.getEstado().toString();
                String score = (enfrentamiento.getScoreParticipante1() != null && enfrentamiento.getScoreParticipante2() != null) ?
                        enfrentamiento.getScoreParticipante1() + " - " + enfrentamiento.getScoreParticipante2() : "-";
                String ganador = enfrentamiento.getGanador() != null ? enfrentamiento.getGanador().getNombre() : "-";

                table.addCell(p1);
                table.addCell(p2);
                table.addCell(estado);
                table.addCell(score);
                table.addCell(ganador);
            }

            document.add(table);
            document.add(new Paragraph("\n"));
        }

        document.close();
        return baos.toByteArray();
    }

    @Transactional(readOnly = true)
    public String generarResumenBracketJSON(Long bracketId) {
        Bracket bracket = bracketRepository.findById(bracketId)
                .orElseThrow(() -> new ResourceNotFoundException("Bracket no encontrado con ID: " + bracketId));

        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"bracketId\": ").append(bracket.getId()).append(",\n");
        json.append("  \"torneoNombre\": \"").append(bracket.getTorneo().getNombre()).append("\",\n");
        json.append("  \"estado\": \"").append(bracket.getEstado()).append("\",\n");
        json.append("  \"rondas\": [\n");

        for (int i = 0; i < bracket.getRondas().size(); i++) {
            Ronda ronda = bracket.getRondas().get(i);
            json.append("    {\n");
            json.append("      \"numeroRonda\": ").append(ronda.getNumeroRonda()).append(",\n");
            json.append("      \"nombre\": \"").append(ronda.getNombre()).append("\",\n");
            json.append("      \"estado\": \"").append(ronda.getEstado()).append("\",\n");
            json.append("      \"enfrentamientos\": [\n");

            for (int j = 0; j < ronda.getEnfrentamientos().size(); j++) {
                Enfrentamiento e = ronda.getEnfrentamientos().get(j);
                json.append("        {\n");
                json.append("          \"participante1\": \"").append(e.getParticipante1() != null ? e.getParticipante1().getNombre() : "TBD").append("\",\n");
                json.append("          \"participante2\": \"").append(e.getParticipante2() != null ? e.getParticipante2().getNombre() : "TBD").append("\",\n");
                json.append("          \"ganador\": \"").append(e.getGanador() != null ? e.getGanador().getNombre() : "-").append("\",\n");
                json.append("          \"estado\": \"").append(e.getEstado()).append("\"\n");
                json.append("        }");
                if (j < ronda.getEnfrentamientos().size() - 1) json.append(",");
                json.append("\n");
            }

            json.append("      ]\n");
            json.append("    }");
            if (i < bracket.getRondas().size() - 1) json.append(",");
            json.append("\n");
        }

        json.append("  ]\n");
        json.append("}\n");

        return json.toString();
    }
}
