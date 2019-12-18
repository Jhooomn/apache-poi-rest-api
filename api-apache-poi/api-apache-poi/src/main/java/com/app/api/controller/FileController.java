package com.app.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.api.dominio.service.FileService;

@RestController
@RequestMapping("/file")
public class FileController {

	private final FileService fileService;

	public FileController(FileService file_Service) {
		this.fileService = file_Service;
	}

	@PostMapping
	public List<Map<String, String>> fileToJSON(@RequestParam("file") MultipartFile file) throws Exception {
		return fileService.upload(file);
	}

}
