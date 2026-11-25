# Rest Api

Use existing /api/projects contract. POST 201 with Location, GET 200, PUT 200, archive DELETE 204 where implemented; 400 invalid input, 404 missing, 409 conflict. Use consistent error format and DTOs. Generate slug on backend; handle empty normalization and collisions, including database races. Keep slug stable on rename unless explicitly changed. Pagination and tenant authorization required before broad production listing. Do not invent endpoints or break existing clients.
