package com.app.api.controller;

import java.io.File;
import java.util.ArrayList;

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
	public ArrayList<String[]> fileToJSON(@RequestParam("file") MultipartFile file) {
		return fileService.upload(file);
	}

}
