package com.jpmc.midascore;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserPopulator {

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private DatabaseConduit databaseConduit;

    public void populate() {

        String[] userLines = fileLoader.loadStrings("/test_data/lkjhgfdsa.hjkl");

        for (String userLine : userLines) {

            if (userLine == null || userLine.trim().isEmpty()) continue;

            // 🔥 handle multi-line issue
            String[] lines = userLine.split("\\r?\\n");

            for (String line : lines) {

                if (line.trim().isEmpty()) continue;

                String[] userData = line.trim().split(",\\s*");

                String name = userData[0].trim();
                float balance = Float.parseFloat(userData[1].trim());

                UserRecord user = new UserRecord(name, balance);
                databaseConduit.save(user);
            }
        }
    }
}