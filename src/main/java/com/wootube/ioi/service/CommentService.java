package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.Comment;
import com.wootube.ioi.domain.repository.CommentRepository;
import com.wootube.ioi.service.dto.CommentRequestDto;
import com.wootube.ioi.service.dto.CommentResponseDto;
import com.wootube.ioi.service.exception.NotFoundCommentException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentResponseDto save(CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.save(new Comment(commentRequestDto.getContents()));
        return new CommentResponseDto(comment.getId(),
                comment.getContents(),
                comment.getUpdateTime());
    }

//    public List<CommentResponseDto> findByVideoId(Long videoId) {
//        return Collections.unmodifiableList(commentRepository.findAllByVideoId(videoId));
//    }

    @Transactional
    //public CommentResponse update(Long videoId, Long commentId, User sessionUser. CommentRequest commentRequest)
    public CommentResponseDto update(Long commentId, CommentRequestDto commentRequestDto) {
        // 같은 비디오인지 확인
        // Video video = videoService.findById(videoId);
        Comment comment = findById(commentId);

        //comment.update(video, sessionUser, contents);
        comment.update(commentRequestDto.getContents());
        return new CommentResponseDto(comment.getId(),
                comment.getContents(),
                comment.getUpdateTime());
    }

    //public void delete(Long videoId, Long commentId, User sessionUser)
    public void delete(Long commentId) {
        // 같은 비디오인지 확인
        // 세션 유저와 comment의 유저가 같은지 확인
        Comment comment = findById(commentId);
//        if(comment.isSameWriteWith(sessionUser)){
        commentRepository.deleteById(commentId);
//        }
//        throw new NotMatchCommentWriterException();
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(NotFoundCommentException::new);
    }
}
