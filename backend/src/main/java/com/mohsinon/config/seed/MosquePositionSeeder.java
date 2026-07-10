package com.mohsinon.config.seed;

import com.mohsinon.modules.mosques.entity.MosquePosition;
import com.mohsinon.modules.mosques.repository.MosquePositionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MosquePositionSeeder implements CommandLineRunner {

    private final MosquePositionRepository repository;

    public MosquePositionSeeder(MosquePositionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {

        createPosition(
                "IMAM",
                "إمام",
                "إمام المسجد",
                true
        );

        createPosition(
                "COMMITTEE_PRESIDENT",
                "رئيس اللجنة",
                "رئيس لجنة تسيير المسجد",
                true
        );

        createPosition(
                "COMMITTEE_VICE_PRESIDENT",
                "نائب الرئيس",
                "نائب رئيس اللجنة",
                true
        );

        createPosition(
                "COMMITTEE_SECRETARY",
                "الكاتب",
                "كاتب اللجنة",
                true
        );

        createPosition(
                "COMMITTEE_TREASURER",
                "أمين المال",
                "مسؤول الشؤون المالية",
                true
        );

        createPosition(
                "COMMITTEE_MEMBER",
                "عضو اللجنة",
                "عضو في لجنة المسجد",
                false
        );

    }

    private void createPosition(
            String code,
            String name,
            String description,
            Boolean uniquePosition
    ) {

        if (repository.existsByCode(code)) {
            return;
        }

        MosquePosition position = new MosquePosition();

        position.setCode(code);
        position.setName(name);
        position.setDescription(description);
        position.setUniquePosition(uniquePosition);
        position.setActive(true);

        repository.save(position);

    }

}