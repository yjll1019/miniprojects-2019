package com.wootube.ioi.service;

import java.io.IOException;

import com.wootube.ioi.domain.model.Video;
import com.wootube.ioi.domain.repository.VideoRepository;
import com.wootube.ioi.exception.FileUploadException;
import com.wootube.ioi.exception.NotFoundVideoException;
import com.wootube.ioi.request.VideoDto;
import com.wootube.ioi.util.FileUploader;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VideoService {
	private final String directoryName = "wootube";
	private final FileUploader fileUploader;
	private final ModelMapper modelMapper;

	private final VideoRepository videoRepository;

	public VideoService(FileUploader fileUploader, ModelMapper modelMapper, VideoRepository videoRepository) {
		this.fileUploader = fileUploader;
		this.modelMapper = modelMapper;
		this.videoRepository = videoRepository;
	}

	public void create(MultipartFile uploadFile, VideoDto videoDto) {
		String videoUrl = uploadFile(uploadFile, directoryName);
		String originFileName = uploadFile.getOriginalFilename();
		Video video = modelMapper.map(videoDto, Video.class);
		video.setContentPath(videoUrl);
		video.setOriginFileName(originFileName);
		videoRepository.save(video);
	}

	public VideoDto findById(Long id) {
		return modelMapper.map(findVideo(id), VideoDto.class);
	}

	private Video findVideo(Long id) {
		return videoRepository.findById(id)
				.orElseThrow(NotFoundVideoException::new);
	}

	public String uploadFile(MultipartFile uploadFile, String directoryName) {
		try {
			return fileUploader.uploadFile(uploadFile, directoryName);
		} catch (IOException e) {
			throw new FileUploadException();
		}
	}
}
