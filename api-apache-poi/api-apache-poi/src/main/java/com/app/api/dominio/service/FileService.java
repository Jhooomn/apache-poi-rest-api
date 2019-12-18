package com.app.api.dominio.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

	public ArrayList<String[]> upload(MultipartFile file) {

		ArrayList<String[]> arrayDatos = new ArrayList<>();
		InputStream excelStream = null;

		try {
			Path tempDir = Files.createTempDirectory("");
			File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
			file.transferTo(tempFile);
			excelStream = new FileInputStream(tempFile);

			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(excelStream);

			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
			HSSFRow hssfRow = hssfSheet.getRow(hssfSheet.getTopRow());
			String[] datos = new String[hssfRow.getLastCellNum()];
			for (Row row : hssfSheet) {
				for (Cell cell : row) {

					datos[cell.getColumnIndex()] = (cell.getCellType() == CellType.STRING) ? cell.getStringCellValue()
							: (cell.getCellType() == CellType.NUMERIC) ? "" + cell.getNumericCellValue()
									: (cell.getCellType() == CellType.BOOLEAN) ? "" + cell.getBooleanCellValue()
											: (cell.getCellType() == CellType.BLANK) ? "BLANK"
													: (cell.getCellType() == CellType.FORMULA) ? "FORMULA"
															: (cell.getCellType() == CellType.ERROR) ? "ERROR" : "";
				}
				arrayDatos.add(datos);
				datos = new String[hssfRow.getLastCellNum()];
			}
		} catch (FileNotFoundException fileNotFoundException) {
			System.out.println("The file not exists (No se encontró el fichero): " + fileNotFoundException);
		} catch (IOException ex) {
			System.out.println("Error in file procesing (Error al procesar el fichero): " + ex);
		} finally {
			try {
				excelStream.close();
			} catch (IOException ex) {
				System.out.println(
						"Error in file processing after close it (Error al procesar el fichero después de cerrarlo): "
								+ ex);
			}
		}
		return arrayDatos;
	}

}
