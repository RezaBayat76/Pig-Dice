package me.rezabayat.pigdice.controller;

import me.rezabayat.pigdice.dto.GameDTO;
import me.rezabayat.pigdice.service.GameService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("games")
@CrossOrigin
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("add-game")
    public void addGame(@RequestBody GameDTO gameDTO, @RequestHeader("Authorization") String token){
        this.gameService.addGame(gameDTO, token);
    }
}
