package com.files.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.files.model.ImageModel;

public interface IModelRepository extends JpaRepository<ImageModel,Long> {

	Optional<ImageModel> findByName(String name);

}
