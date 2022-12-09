package net.intelie.challenges;

import java.util.List;

public class EventIteratorImplementation implements EventIterator {

//	list of all the events
	private final List<Event> eventList;
//	iterator index
	private int i;
	
//	Constructor
	public EventIteratorImplementation(List<Event> eventList) {
		this.eventList = eventList;
		i = -1;
	}


	@Override
	public boolean moveNext() {
//		iteration
		i++;
//		returns true until it reaches the end of the list
		return i < eventList.size();
	}

	
	@Override
	public Event current() {
//		if the index does not exist, throws an error
		if (i < 0 || i > eventList.size()) {
			throw new IllegalStateException("The element does not exist.");
		}
//		returns the current element of the eventList
		return eventList.get(i);
	}


	@Override
	public void remove() {
		if (i < 0 || i > eventList.size()) {
			throw new IllegalStateException("The element does not exist.");
		} else {
			eventList.remove(i);
		}
	}
	
	
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
