package ru.caselab.filehandler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.caselab.filehandler.model.File;

public interface FileRepository extends JpaRepository<File, Long> {

}
