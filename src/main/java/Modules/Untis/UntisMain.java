package Modules.Untis;

import org.bytedream.untis4j.Session;
import org.bytedream.untis4j.responseObjects.Timetable;

import java.io.IOException;
import java.time.LocalDate;

public class UntisMain {
	
	public static void main(String[] args) throws IOException {
		
		Session sassy = null;
		
		Timetable timetable = sassy.getTimetableFromClassId(
				LocalDate.of(2022, 6, 20),
				LocalDate.of(2022, 6, 24),
				sassy.getClasses().searchByName("11EL2B").get(0).getId());
		System.out.println();
		for (int i = 0; i < timetable.size(); i++) {
			System.out.println("Lesson " + (i+1) + ": " + timetable.get(i).getSubjects().toString());
		}
		
		// logout
		sassy.logout();
		
	}
	
}
