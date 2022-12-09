package net.intelie.challenges;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EventStoreImplementationTest {

	Event event1 = new Event("TestEvent", 5);
	Event event2 = new Event("TestEvent", 10);
	Event event3 = new Event("TestEvent", 13);
	Event event4 = new Event("event4", 27);
	Event event5 = new Event("event5", 44);

	EventStore eventStore = new EventStoreImplementation();
	EventIterator eventIterator;

//	calls this method before every test
	@Before
	public void createEventStore() throws Exception {
		eventStore.insert(event1);
		eventStore.insert(event2);
		eventStore.insert(event3);
		eventStore.insert(event4);
		eventStore.insert(event5);
	}

	@Test
	public void insertNullEvent() {
		Event event = null;
		try {
			eventStore.insert(event);
//			used instead of the assertTrue(false), but fail() is clearer
			fail();
		} catch (NullPointerException e) {
			String expectedMessage = "You must provide an event name.";
			String actualMessage = e.getMessage();
		    assertTrue(actualMessage.contains(expectedMessage));
		}
	}

	@Test
	public void insertNullEventType() {
		Event event = new Event(null, 0);
		try {
			eventStore.insert(event);
			fail();
		} catch (NullPointerException e) {
			String expectedMessage = "You must provide an event type.";
			String actualMessage = e.getMessage();
		    assertTrue(actualMessage.contains(expectedMessage));		}
	}

	@Test
	public void insertSameEventType() {
		Event eventN = new Event("event5", 44);
		try {
			eventStore.insert(eventN);
			Assert.assertTrue(true);
		} catch (NullPointerException e) {
			fail();
		}
	}

	@Test
	public void removeNullEventType() {
		try {
			eventStore.removeAll(null);
			fail();
		} catch (NullPointerException e) {
			Assert.assertTrue(e.getMessage().equals("You must provide the event type you want to delete."));
		}
	}


	@Test
	public void queryIteratorRemove() {
		eventIterator = eventStore.query("TestEvent", 0, 10);
		while (eventIterator.moveNext()) {
			eventIterator.remove();
		}
		eventIterator = eventStore.query("TestEvent", 0, 10);
		assertFalse(eventIterator.moveNext());
	}

//	release alocated resources
	@After
	public void end() {
		eventStore.removeAll("type");
	}
}