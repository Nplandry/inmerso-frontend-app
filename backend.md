GET /health — Sin autenticación. Verifica que el servidor esté vivo. Ejemplo: curl https://inmerso-backend.onrender.com/health → {"status":"ok","service":"inmerso-ai"}

GET /setup — Sin autenticación. Página HTML de configuración/onboarding para Atajos de iOS. Ejemplo: curl https://inmerso-backend.onrender.com/setup → HTML.

GET /api/v1/focus/status — Requiere header x-api-key. Devuelve tarea actual y próxima tarea del calendario. Ejemplo: curl -H "x-api-key: *** https://inmerso-backend.onrender.com/api/v1/focus/status → {"status":"success","data":{"current_task":null,"next_task":null,"time_remaining":{"current_ends_in_minutes":0,"next_starts_in_minutes":null}}}

POST /api/v1/focus/schedule — Requiere header x-api-key. Agenda un bloque en Google Calendar. Acepta JSON con text o multipart con archivo audio. Ejemplo texto: curl -X POST -H "x-api-key: *** -H "Content-Type: application/json" -d '{"text":"reunión de 30 minutos a las 5 de la tarde"}' https://inmerso-backend.onrender.com/api/v1/focus/schedule → {"status":"success","message":"Bloque agendado con éxito","scheduled_event":{"title":"Reunión","start":"2026-09-29T17:00:00-03:00","end":"2026-09-29T17:30:00-03:00"},"meta":{"reasoning":"..."}}

Ejemplo con audio: curl -X POST -H "x-api-key: *** -F "audio=@audio.m4a" https://inmerso-backend.onrender.com/api/v1/focus/schedule

Error sin API key: {"status":"error","message":"No autorizado. Proporciona una x-api-key válida"}