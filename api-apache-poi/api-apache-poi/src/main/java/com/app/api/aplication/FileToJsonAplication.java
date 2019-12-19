package com.app.api.aplication;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import org.springframework.web.multipart.MultipartFile;

import com.app.api.mapper.FileToJsonMapper;

public class FileToJsonAplication {

	FileToJsonMapper fileToJsonMapper;

	public FileToJsonAplication(FileToJsonMapper fl) {
		this.fileToJsonMapper = fl;
	}

	public List<Map<String, String>> upload(MultipartFile file) throws Exception {

		Path tempDir = Files.createTempDirectory("");
		File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
		file.transferTo(tempFile);
		Workbook workbook = WorkbookFactory.create(tempFile);
		Sheet sheet = workbook.getSheetAt(0);

		Supplier<Stream<Row>> rowStreamSupplier = fileToJsonMapper.getRowStreamSupplier(sheet);
		Row headerRow = rowStreamSupplier.get().findFirst().get();

		Iterator it = sheet.iterator();
		while (it.hasNext()) {
			Row row = (Row) it.next();
			for (int cn = 0; cn < row.getLastCellNum(); cn++) {
				Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			}
		}

		List<String> headerCells = StreamSupport.stream(headerRow.spliterator(), false).map(c -> c.toString())
				.collect(Collectors.toList());

		int colCount = headerCells.size();

		return rowStreamSupplier.get().skip(1).map(row -> {

			List<String> cellList = StreamSupport.stream(row.spliterator(), false).map(c -> c.toString())
					.collect(Collectors.toList());

			return fileToJsonMapper.cellIteratorSupplier(colCount).get().collect(
					Collectors.toMap(x -> headerCells.get(x), y -> cellList.get(y), (oldValue, newValue) -> newValue));

		}).collect(Collectors.toList());

	}

}
