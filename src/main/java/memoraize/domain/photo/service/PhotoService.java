package memoraize.domain.photo.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import memoraize.domain.photo.repository.UuidRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoService {

	private final UuidRepository uuidRepository;

	public void makeUuid(){
		String uuid = UUID.randomUUID().toString();
		String save = uuidRepository.save(uuid);
	}

}
