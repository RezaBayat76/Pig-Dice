package me.rezabayat.pigdice.controller;

import me.rezabayat.pigdice.dto.GameDTO;
import me.rezabayat.pigdice.service.GameService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("games")
@CrossOrigin
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public List<GameDTO> games(){
        return this.gameService.games();
    }
    @PostMapping("add-game")
    public void addGame(@RequestBody GameDTO gameDTO, @RequestHeader("Authorization") String token){
        this.gameService.addGame(gameDTO, token);
    }
}
