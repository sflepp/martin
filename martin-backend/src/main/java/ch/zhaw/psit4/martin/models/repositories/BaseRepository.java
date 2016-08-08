package ch.zhaw.psit4.martin.models.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {
	T findOne(ID id);
	
	@Transactional(readOnly = true)
	List<T> findAll();
	
	@Transactional(readOnly = true)
	List<T> findAll(Pageable pageable);
}
