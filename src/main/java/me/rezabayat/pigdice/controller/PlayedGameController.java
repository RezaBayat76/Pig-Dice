package me.rezabayat.pigdice.controller;

import me.rezabayat.pigdice.dto.CommentOnPlayedGame;
import me.rezabayat.pigdice.dto.GameCommentDTO;
import me.rezabayat.pigdice.dto.PlayedGameCommentDTO;
import me.rezabayat.pigdice.service.PlayedGameService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("played-games")
@CrossOrigin
public class PlayedGameController {
    private final PlayedGameService playedGameService;

    public PlayedGameController(PlayedGameService playedGameService) {
        this.playedGameService = playedGameService;
    }

    @PostMapping("add-comment")
    public void addComment(@RequestBody CommentOnPlayedGame commentOnPlayedGame, @RequestHeader("Authorization") String token) {
        this.playedGameService.addComment(commentOnPlayedGame, token);
    }

    @GetMapping("comments/{id}")
    public List<PlayedGameCommentDTO> comments(@PathVariable("id") long id) {
        return this.playedGameService.comments(id);
    }

}
