package com.example.schedulemanagementadvanced.user.repository;

import com.example.schedulemanagementadvanced.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email); // 가입된 이메일인지 확인

    Optional<User> findByEmail(String email); // 로그인할 때 이메일로 유저 정보 가져옴, 없을 수 있으니 Optional 처리

    default User findByEmailOrElseThrow(String email){
        return findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 이메일의 유저를 찾을 수 없습니다."));
    } // 없으면 존재하지 않는다는 에러를 Throw
}
