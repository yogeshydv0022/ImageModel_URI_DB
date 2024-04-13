package com.files.services;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.files.excepition.FileNotFoundException;
import com.files.excepition.FileStorageException;
import com.files.model.ImageModel;
import com.files.repository.IModelRepository;

@Service
public class ImageService {

	@Autowired
	IModelRepository modelRepository;

	// Upload File
	public ImageModel UploadFile(MultipartFile file) {
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}

			ImageModel imageModel = new ImageModel(fileName, file.getContentType(), file.getBytes());

			return modelRepository.save(imageModel);
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	// Get File By Id
	public ImageModel getFile(long fileId) {
		return modelRepository.findById(fileId)
				.orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));
	}

	// Get File By Name
	public ImageModel getFileByName(String name) {
		return modelRepository.findByName(name)
				.orElseThrow(() -> new FileNotFoundException("File not found with id " + name));
	}

	// get All
	public Stream<ImageModel> getAllFiles() {
		return modelRepository.findAll().stream();
	}

	//
	public void DeleteById(long id) {
		modelRepository.deleteById(id);
	}

	public ImageModel updateById(long id, MultipartFile file) throws IOException {
		ImageModel model = modelRepository.findById(id)
				.orElseThrow(() -> new FileNotFoundException("File not found with id " + id));
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		model.setName(fileName);
		model.setType(file.getContentType());
		model.setData(file.getBytes());
		return modelRepository.save(model);

	}

}
