package com.app.api.dominio.service;

import static java.util.stream.Collectors.toMap;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.api.util.FileUtil;

@Service
public class FileService {

	FileUtil fileUtil;

	public FileService(FileUtil fl) {
		this.fileUtil = fl;
	}

	public List<Map<String, String>> upload(MultipartFile file) throws Exception {

		Path tempDir = Files.createTempDirectory("");

		File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();

		file.transferTo(tempFile);

		Workbook workbook = WorkbookFactory.create(tempFile);

		Sheet sheet = workbook.getSheetAt(0);

		Supplier<Stream<Row>> rowStreamSupplier = fileUtil.getRowStreamSupplier(sheet);

		Row headerRow = rowStreamSupplier.get().findFirst().get();

		List<String> headerCells = StreamSupport.stream(headerRow.spliterator(), false).map(c -> c.toString())
				.collect(Collectors.toList());
		int colCount = headerCells.size();

		return rowStreamSupplier.get().skip(2).map(row -> {
			List<String> cellList = StreamSupport.stream(row.spliterator(), false).map(c -> c.toString())
					.collect(Collectors.toList());
			return fileUtil.cellIteratorSupplier(colCount).get()
					.collect(toMap(index -> headerCells.get(index), index -> cellList.get(index)));
		}).collect(Collectors.toList());

	}

}
