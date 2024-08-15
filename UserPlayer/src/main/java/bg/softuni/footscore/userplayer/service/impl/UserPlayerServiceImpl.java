package bg.softuni.footscore.userplayer.service.impl;

import bg.softuni.footscore.userplayer.model.dto.CreateUserPlayerDto;
import bg.softuni.footscore.userplayer.model.dto.UserPlayerDto;
import bg.softuni.footscore.userplayer.model.entity.UserPlayer;
import bg.softuni.footscore.userplayer.repository.UserPlayerRepository;
import bg.softuni.footscore.userplayer.service.UserPlayerService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPlayerServiceImpl implements UserPlayerService {
    private final UserPlayerRepository userPlayerRepository;
    private final ModelMapper modelMapper;

    public UserPlayerServiceImpl(UserPlayerRepository userPlayerRepository,
                                 ModelMapper modelMapper) {
        this.userPlayerRepository = userPlayerRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public void addUserPlayer(CreateUserPlayerDto dto) {
        this.userPlayerRepository.save(this.modelMapper.map(dto, UserPlayer.class));
    }

    @Override
    public List<UserPlayerDto> getAllUserPlayers() {
        return this.userPlayerRepository.findAll().stream().map(p -> this.modelMapper.map(p, UserPlayerDto.class)).toList();
    }

    @Override
    public List<UserPlayerDto> getAllUserPlayersById(Long id) {
        return this.userPlayerRepository.findAllByUserId(id).stream().map(p -> this.modelMapper.map(p, UserPlayerDto.class)).toList();
    }

    @Override
    public void deleteUserPlayerById(Long id) {
        this.userPlayerRepository.deleteById(id);
    }

    @Override
    public UserPlayerDto getUserPlayerById(Long id) {
        return this.userPlayerRepository.findById(id)
                .map(p -> this.modelMapper.map(p, UserPlayerDto.class))
                .orElse(null);
    }

    @Override
    public UserPlayerDto updateUserPlayerById(Long id, UserPlayerDto dto) {
        UserPlayer existingUserPlayer = userPlayerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UserPlayer not found"));

        existingUserPlayer.setName(dto.getName());
        existingUserPlayer.setPosition(dto.getPosition());
        existingUserPlayer.setRating(dto.getRating());
        existingUserPlayer.setAge(dto.getAge());

        UserPlayer updatedUserPlayer = userPlayerRepository.save(existingUserPlayer);

        return this.modelMapper.map(updatedUserPlayer, UserPlayerDto.class);
    }
}
