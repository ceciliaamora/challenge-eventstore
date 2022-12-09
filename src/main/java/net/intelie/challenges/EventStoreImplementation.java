package net.intelie.challenges;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class EventStoreImplementation implements EventStore {
	
//	Map interface used for performance improvement, assigns key to value
	private Map<String, List<Event>> events;
	
	public EventStoreImplementation() {
/*		
* 		ConcurrentHashMap is a ConcurrentMap implementation 
*		to guarantee memory consistency in a multi-threading environment 
*		and does not allow null keys or values. This choice provides better performance in general
*		than Collections.synchronizedMap() and it also performs better when there are much more write
*		than read operations, as this is the case.
*/		
		events = new ConcurrentHashMap<>();
	}

	@Override
//	reserved word 'synchronized' to guarantee thread-safety
	public synchronized void insert(Event event) {
//		isNull() is a method from Objects class and it checks whether the Java object is null or not
		if (Objects.isNull(event)) {
			throw new NullPointerException("You must provide an event name.");
		}

		if (Objects.isNull(event.type())) {
			throw new NullPointerException("You must provide an event type.");
		}

//		Vector<>() was used instead of ArrayList<>() because the former is thread-safe
		List<Event> eventList = new Vector<>();
		eventList.add(event);
		events.put(event.type(), eventList);

	}

	@Override
	public synchronized void removeAll(String type) {
			if (Objects.isNull(type)) {
				throw new NullPointerException("You must provide the event type you want to delete.");
			}
		
//			removing all the events that have the specified type
			events.remove(type);
	}

	@Override
	public EventIterator query(String type, long startTime, long endTime) {
		if (Objects.isNull(type)) {
			throw new NullPointerException("You must provide the event type.");
		}

		List<Event> eventList = new ArrayList<>();
		
		if (events.containsKey(type)) {

/*			Since there is no need to store these results, Stream API was used 
*			because it's less verbose. Its method parallelStream() simplifies 
*			multithreading by running operations in parallel mode.
*/
			eventList = events.get(type).parallelStream()
//					startTime must be inclusive and endTime exclusive, get only valid timestamps
					.filter(event -> event.timestamp() >= startTime && event.timestamp() < endTime)
//					gets all tasks to work and returns a list result
                    .collect(Collectors.toList());
		} else {
			throw new NullPointerException("The type " + type + " does not exist.");
		}

		return new EventIteratorImplementation(eventList);
	}

}
 