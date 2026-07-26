TRUNCATE TABLE test_results;

INSERT INTO test_results (id, test_id, user_id, correct_answers, total_question_count, percentage, passed_date, is_passed)
VALUES (0, 0, 2, 3, 3, 100.00, DATEADD('DAY', -1, NOW()), TRUE);

INSERT INTO test_results (id, test_id, user_id, correct_answers, total_question_count, percentage, passed_date, is_passed)
VALUES (1, 0, 4, 1, 3, 33.33, DATEADD('DAY', -4, NOW()), FALSE);

INSERT INTO test_results (id, test_id, user_id, correct_answers, total_question_count, percentage, passed_date, is_passed)
VALUES (2, 0, 6, 2, 3, 66.67, DATEADD('DAY', -3, NOW()), TRUE);


INSERT INTO test_results (id, test_id, user_id, correct_answers, total_question_count, percentage, passed_date, is_passed)
VALUES (3, 1, 2, 1, 3, 33.33, DATEADD('DAY', -2, NOW()), FALSE);

INSERT INTO test_results (id, test_id, user_id, correct_answers, total_question_count, percentage, passed_date, is_passed)
VALUES (4, 1, 4, 3, 3, 100.00, NOW(), TRUE);

INSERT INTO test_results (id, test_id, user_id, correct_answers, total_question_count, percentage, passed_date, is_passed)
VALUES (5, 1, 6, 1, 3, 33.33, DATEADD('DAY', -7, NOW()), FALSE);

