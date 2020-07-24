package org.management.asset.services;

import org.management.asset.bo.Process;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Haytham DAHRI
 * Spécification du service des processus
 */
public interface ProcessService {

    Process saveProcess(Process process);

    Process updateProcessStatus(String id, boolean status);

    boolean deleteProcess(String id);

    Process getProcess(String id);

    List<Process> getProcesses();

    Long getProcessesCounter();

    Page<Process> getProcesses(int page, int size);

    Page<Process> getProcesses(String search, int page, int size);

}
