package ru.practicum.ewm.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.repository.user.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    public User get(int id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException(
                String.format("User id=%s not found", id)));
    }

    public User update(int id, User user) {
        User userUpdated = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException(
                String.format("User id=%s not found", id)));
        userUpdated.setName(user.getName());
        userUpdated.setEmail(userUpdated.getEmail());
        return userRepository.save(userUpdated);
    }

    @Override
    public void remove(int id) {
        userRepository.deleteById(id);
    }

    public List<User> getUsers(List<Integer> ids, int from, int size) {
        if (ids == null || ids.isEmpty())
            return userRepository.findAll(PageRequest.of(from / size, size)).getContent();
        return userRepository.findByIdIn(ids, PageRequest.of(from / size, size));
    }
}
