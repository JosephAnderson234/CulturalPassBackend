package com.culturalpass.culturalpass.Mail.domain;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class TicketGeneratorService {

    public byte[] generateTicketPDF(String userName, String eventTitle, String eventDate, byte[] qrCodeImage) throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A5);
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.WHITE);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new BaseColor(139, 69, 19));
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);

        PdfPTable mainTable = new PdfPTable(1);
        mainTable.setWidthPercentage(90);
        mainTable.setSpacingBefore(20f);

        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(new BaseColor(139, 69, 19)); // #8B4513
        headerCell.setBorder(Rectangle.NO_BORDER);
        headerCell.setPadding(10);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        Paragraph headerPara = new Paragraph("🎟️ BOLETO DE ENTRADA", titleFont);
        headerPara.setAlignment(Element.ALIGN_CENTER);
        headerCell.addElement(headerPara);
        mainTable.addCell(headerCell);

        PdfPCell badgeCell = new PdfPCell();
        badgeCell.setBackgroundColor(new BaseColor(248, 248, 220)); // #FFF8DC
        badgeCell.setBorder(Rectangle.NO_BORDER);
        badgeCell.setPadding(5);

        Paragraph badgePara = new Paragraph("✓ INSCRIPCIÓN CONFIRMADA",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, new BaseColor(218, 165, 32)));
        badgePara.setAlignment(Element.ALIGN_CENTER);
        badgeCell.addElement(badgePara);
        mainTable.addCell(badgeCell);

        PdfPCell greetingCell = new PdfPCell();
        greetingCell.setBackgroundColor(new BaseColor(248, 248, 220));
        greetingCell.setBorder(Rectangle.NO_BORDER);
        greetingCell.setPadding(8);

        Paragraph greetingPara = new Paragraph("¡Hola " + userName + "!", headerFont);
        greetingPara.setAlignment(Element.ALIGN_CENTER);
        greetingCell.addElement(greetingPara);
        mainTable.addCell(greetingCell);

        PdfPCell eventInfoCell = new PdfPCell();
        eventInfoCell.setBackgroundColor(new BaseColor(245, 222, 179)); // #F5DEB3
        eventInfoCell.setBorder(Rectangle.LEFT);
        eventInfoCell.setBorderColor(new BaseColor(205, 133, 63)); // #CD853F
        eventInfoCell.setBorderWidth(3);
        eventInfoCell.setPadding(10);

        Paragraph eventLabel = new Paragraph("Te has inscrito exitosamente al evento:", normalFont);
        eventLabel.setSpacingAfter(4);
        eventInfoCell.addElement(eventLabel);

        Paragraph eventName = new Paragraph(eventTitle,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, new BaseColor(139, 69, 19)));
        eventName.setSpacingAfter(6);
        eventInfoCell.addElement(eventName);

        Paragraph eventDatePara = new Paragraph("📅 " + eventDate, boldFont);
        eventDatePara.setAlignment(Element.ALIGN_LEFT);
        eventInfoCell.addElement(eventDatePara);

        mainTable.addCell(eventInfoCell);

        PdfPCell instructionCell = new PdfPCell();
        instructionCell.setBackgroundColor(BaseColor.WHITE);
        instructionCell.setBorder(Rectangle.NO_BORDER);
        instructionCell.setPadding(8);

        Paragraph instructionPara = new Paragraph(
                "Presenta este código QR al personal en la recepción del evento",
                boldFont);
        instructionPara.setAlignment(Element.ALIGN_CENTER);
        instructionPara.setSpacingAfter(5);
        instructionCell.addElement(instructionPara);
        mainTable.addCell(instructionCell);

        PdfPCell qrCell = new PdfPCell();
        qrCell.setBackgroundColor(BaseColor.WHITE);
        qrCell.setBorder(Rectangle.NO_BORDER);
        qrCell.setPadding(10);
        qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        try {
            Image qrImage = Image.getInstance(qrCodeImage);
            qrImage.scaleToFit(120, 120);
            qrImage.setAlignment(Element.ALIGN_CENTER);
            qrCell.addElement(qrImage);
        } catch (Exception e) {
            Paragraph qrFallback = new Paragraph("[Código QR]", normalFont);
            qrFallback.setAlignment(Element.ALIGN_CENTER);
            qrCell.addElement(qrFallback);
        }

        mainTable.addCell(qrCell);

        PdfPCell footerCell = new PdfPCell();
        footerCell.setBackgroundColor(new BaseColor(245, 245, 245)); // #F5F5F5
        footerCell.setBorder(Rectangle.NO_BORDER);
        footerCell.setPadding(6);

        Paragraph footerPara = new Paragraph(
                "Válido únicamente para el evento registrado • Conserva este boleto",
                FontFactory.getFont(FontFactory.HELVETICA, 8, new BaseColor(102, 102, 102)));
        footerPara.setAlignment(Element.ALIGN_CENTER);
        footerCell.addElement(footerPara);
        mainTable.addCell(footerCell);

        PdfPCell mainFooterCell = new PdfPCell();
        mainFooterCell.setBackgroundColor(new BaseColor(139, 69, 19));
        mainFooterCell.setBorder(Rectangle.NO_BORDER);
        mainFooterCell.setPadding(8);

        Paragraph mainFooterPara1 = new Paragraph(
                "¡Te esperamos para vivir una experiencia cultural única! 🌟",
                FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.WHITE));
        mainFooterPara1.setAlignment(Element.ALIGN_CENTER);
        mainFooterPara1.setSpacingAfter(3);
        mainFooterCell.addElement(mainFooterPara1);

        Paragraph mainFooterPara2 = new Paragraph(
                "Importante: Llega 15 minutos antes del evento",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.WHITE));
        mainFooterPara2.setAlignment(Element.ALIGN_CENTER);
        mainFooterCell.addElement(mainFooterPara2);
        mainTable.addCell(mainFooterCell);

        document.add(mainTable);
        document.close();

        return baos.toByteArray();
    }
}
