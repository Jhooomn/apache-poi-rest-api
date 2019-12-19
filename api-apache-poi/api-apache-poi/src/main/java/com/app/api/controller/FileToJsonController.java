package com.app.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.api.aplication.FileToJsonAplication;
import com.app.api.mapper.FileToJsonMapper;

@RestController
@RequestMapping("/file")
public class FileToJsonController {

	FileToJsonAplication fileToJsonAplication;

	public FileToJsonController(@Autowired FileToJsonMapper fileToJsonMapper) {
		this.fileToJsonAplication = new FileToJsonAplication(fileToJsonMapper);
	}

	@PostMapping
	public List<Map<String, String>> fileToJSON(@RequestParam("file") MultipartFile file) throws Exception {
		return fileToJsonAplication.upload(file);
	}

}
