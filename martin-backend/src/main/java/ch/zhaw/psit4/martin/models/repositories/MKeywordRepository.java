package ch.zhaw.psit4.martin.models.repositories;


import ch.zhaw.psit4.martin.models.MKeyword;

public interface MKeywordRepository extends BaseRepository<MKeyword, Integer>{
	MKeyword findByKeywordIgnoreCase(String keyword);
}
