package ch.zhaw.psit4.martin.models.repositories;

import ch.zhaw.psit4.martin.models.MPlugin;

public interface MPluginRepository extends BaseRepository<MPlugin, Integer>{
	// = @Query("SELECT MPlugin p WHERE p.uuid = :uuid")
	MPlugin findByUuid(String uuid);

}
