package com.files.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.files.model.ImageModel;
import com.files.payload.Response;
import com.files.services.ImageService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/images")
public class ImageModelController {

	@Autowired
	private ImageService imageService;

	@PostMapping("/uploadFile")
	public Response uploadFile(@RequestParam("file") MultipartFile file) {
		ImageModel files = imageService.UploadFile(file);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/images/downloadFile/")
				.path(files.getName()).toUriString();

		return new Response(files.getId(),files.getName(), fileDownloadUri, file.getContentType(), file.getSize());
	}

	@PostMapping("/uploadFiles")
	public List<Response> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
		return Arrays.asList(files).stream().map(file -> uploadFile(file)).collect(Collectors.toList());
	}

//	@GetMapping("/downloadFile/{fileId:.+}")
//	public ResponseEntity<Resource> downloadFilebyId(@PathVariable String fileId, HttpServletRequest request) {
//		// Load file as Resource
//		ImageModel imageDb = imageService.getFile(fileId);
//
//		return ResponseEntity.ok().contentType(MediaType.parseMediaType(imageDb.getType()))
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageDb.getName() + "\"")
//				.body(new ByteArrayResource(imageDb.getData()));
//	}

	//

	@GetMapping("/")
	public ResponseEntity<List<Response>> getListFiles() {
		List<Response> files = imageService.getAllFiles().map(dbFile -> {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/images/downloadFile/")
					.path(dbFile.getName()).toUriString();

			return new Response(dbFile.getId(),dbFile.getName(), fileDownloadUri, dbFile.getType(), dbFile.getData().length);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(files);
	}
	
	//
	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFileByName(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		ImageModel imageDb = imageService.getFileByName(fileName);

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(imageDb.getType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageDb.getName() + "\"")
				.body(new ByteArrayResource(imageDb.getData()));
	}
	
	 @PutMapping("/update")
     public ResponseEntity<?> updateImage(@RequestParam("id") long id ,@RequestParam("files") MultipartFile files) throws IOException {
         return ResponseEntity.ok(imageService.updateById(id,files));
     }
	 
	 @DeleteMapping("/delete/{id}")
	 public ResponseEntity<String> Delete(@PathVariable long id){
		 imageService.DeleteById(id);
         return ResponseEntity.status(HttpStatus.OK).body("Image Reecord Deleted : "+id);
     }
}
