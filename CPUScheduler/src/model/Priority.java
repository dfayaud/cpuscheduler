package model;

import java.util.ArrayList;
import java.util.List;

public class Priority {
	private int timeframe;
	private int[] jobQueue;
	private Process[] processes;
	private List<Process> processesList;
	
	public Priority(int timeframe, Process[] processes) {
		this.timeframe = timeframe;
		this.processes = processes;
		createProcessList();
		fillJobQueue();
	}

	public void fillJobQueue() {
		int timer = 0;
		int index = 0;
		this.jobQueue = new int[timeframe];
		List<Process> localProcessesList = new ArrayList<Process>();
		sortByArrivalTime();
		while (timer != jobQueue.length && index < processes.length) {
			if (localProcessesList.isEmpty()) {
				localProcessesList.add(processesList.get(index));
			}
			for (int i = 0; i < processes.length; i++) {
				if (timer >= processesList.get(i).getArrivalTime()
						&& !localProcessesList.contains(processesList.get(i))) {
					localProcessesList.add(processesList.get(i));
				}
				if(index == 0 && !localProcessesList.isEmpty()) {
					sortByPriority(localProcessesList);
				}
			}
			if (index >= localProcessesList.size() || localProcessesList.get(index).getArrivalTime() > timer) {
				jobQueue[timer] = 0;
				timer++;
			} else if (localProcessesList.get(index).getBurstTime() == 0) {
				localProcessesList.get(index).setPriorityLevel(-1);
				processes[localProcessesList.get(index).getProcessNumber() - 1].setWaitingTime(
						timer - processes[localProcessesList.get(index).getProcessNumber() - 1].getArrivalTime()
								- processes[localProcessesList.get(index).getProcessNumber() - 1].getBurstTime());
				processes[localProcessesList.get(index).getProcessNumber() - 1].setTurnaroundTime(
						processes[localProcessesList.get(index).getProcessNumber() - 1].getWaitingTime()
								+ processes[localProcessesList.get(index).getProcessNumber() - 1].getBurstTime());
				index++;
				sortByPriority(localProcessesList);
			} else {

				this.jobQueue[timer] = localProcessesList.get(index).getProcessNumber();
				localProcessesList.get(index).setBurstTime(localProcessesList.get(index).getBurstTime() - 1);

				timer++;
			}
		}

	}

	private void sortByPriority(List<Process> localProcessesList) {
		localProcessesList.sort((Process a, Process b) -> a.getPriorityLevel() - b.getPriorityLevel());
		
	}


	private void sortByArrivalTime() {
		processesList.sort((Process a, Process b) -> a.getArrivalTime() - b.getArrivalTime());

	}

	public int[] getJobQueue() {
		return jobQueue;
	}

	public void createProcessList() {
		this.processesList = new ArrayList<Process>();
		for (int i = 0; i < processes.length; i++) {
			processesList.add(new Process(processes[i]));
		}
	}
}
