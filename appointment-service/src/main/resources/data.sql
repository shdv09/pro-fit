INSERT INTO trainers (first_name, last_Name, gender, birth_date, specialization)
    SELECT 'Chuck', 'Norris', 'M', '1940-03-10', 'COMBAT'
    WHERE NOT EXISTS (
      SELECT 1 FROM trainers
      WHERE first_name = 'Chuck' AND last_Name = 'Norris'
    );
INSERT INTO trainers (first_name, last_Name, gender, birth_date, specialization)
    SELECT 'Jane', 'Fonda', 'F', '1937-12-21', 'AEROBICS'
    WHERE NOT EXISTS (
          SELECT 1 FROM trainers
          WHERE first_name = 'Jane' AND last_Name = 'Fonda'
    );
INSERT INTO trainers (first_name, last_Name, gender, birth_date, specialization)
    SELECT 'Arnold', 'Schwarzenegger', 'M', '1947-07-30','BODYBUILDING'
    WHERE NOT EXISTS (
              SELECT 1 FROM trainers
              WHERE first_name = 'Arnold' AND last_Name = 'Schwarzenegger'
    );

INSERT INTO time_slots (workout_date, workout_hour, trainer_id, client_id) VALUES ('2024-07-15', 8, 1, 2);
INSERT INTO time_slots (workout_date, workout_hour, trainer_id, client_id) VALUES ('2024-07-15', 21, 2, 1);
