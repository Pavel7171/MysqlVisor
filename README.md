# MysqlVisor
ПО для работы с данными в таблицах Mysql
Возможности:
- Изменение значений в ячейках
- Удаление строк
- Добавление строк путем загрузки csv файла

Требования к таблице для редактирования данных:
- первый столбец ID not_null,autoincrement,PRI

Требования к файлу для загрузки данных:
- разделители ","
- кол-во элементов разделенных запятыми должны соответствовать кол-ву колонок в таблице без учета 1 ID (если значение отстутствует, разделитель должен быть)
