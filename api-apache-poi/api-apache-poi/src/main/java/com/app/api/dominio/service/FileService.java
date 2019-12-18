package com.app.api.dominio.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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

	public List<String[]> upload(MultipartFile file) {

		ArrayList<String[]> data = new ArrayList<>();
		InputStream excelStream = null;

		try {
			// Temporal file directory
			Path tempDir = Files.createTempDirectory("");
			File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
			// Storage the file in a temporal directory
			file.transferTo(tempFile);
			excelStream = new FileInputStream(tempFile);

			// HSSFWorkbook --> Excel file
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(excelStream);

			// HSSFSheet --> Excel sheet
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

			// HSSFRow --> Excel Row
			HSSFRow hssfRow = hssfSheet.getRow(hssfSheet.getTopRow());

			// I'll send all the data in an array called: info
			String[] info = new String[hssfRow.getLastCellNum()];

			for (Row row : hssfSheet) {
				for (Cell cell : row) {

					info[cell.getColumnIndex()] = (cell.getCellType() == CellType.STRING) ? cell.getStringCellValue()
							: (cell.getCellType() == CellType.NUMERIC) ? "" + cell.getNumericCellValue()
									: (cell.getCellType() == CellType.BOOLEAN) ? "" + cell.getBooleanCellValue()
											: (cell.getCellType() == CellType.BLANK) ? "BLANK"
													: (cell.getCellType() == CellType.FORMULA) ? "FORMULA"
															: (cell.getCellType() == CellType.ERROR) ? "ERROR" : "";
				}
				data.add(info);
				info = new String[hssfRow.getLastCellNum()];
			}
		} catch (FileNotFoundException fileNotFoundException) {
			System.out.println("The file not exists: " + fileNotFoundException);
		} catch (IOException ex) {
			System.out.println("Error in file procesing : " + ex);
		} finally {
			try {
				excelStream.close();
			} catch (IOException ex) {
				System.out.println("Error in file processing after close it : " + ex);
			}
		}
		return data;
	}

}
