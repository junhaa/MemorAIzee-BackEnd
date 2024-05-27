package memoraize.domain.voice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import memoraize.domain.voice.entity.Voice;

public interface VoiceRepository extends JpaRepository<Voice, Long> {
	Optional<Voice> findByUserId(Long userId);
}
