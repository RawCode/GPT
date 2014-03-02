package rc.ubt.auto;

public class ConcurrentSave
{
	//this is complex class that will unload save and GC chunk objects from server based on it's internal logic.
	//additional classes will be used to replace "vanilla" implementation of saving\loading and collecting.
	//also shoud collect other object types if too many spawned in single chunk
	//also shoud load \ unload chunks and keep age of each loaded chunk
	//there is no reason to waste time on chunks that not altered in any way by game
}
