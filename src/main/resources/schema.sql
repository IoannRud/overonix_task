CREATE TABLE IF NOT EXISTS coordinates
(
    id           SERIAL PRIMARY KEY,
    lat          VARCHAR NOT NULL,
    lon          VARCHAR NOT NULL,
    display_name VARCHAR,
    address      JSONB   NOT NULL DEFAULT '{}'::JSONB
);
CREATE UNIQUE INDEX IF NOT EXISTS lat_lon_unique_idxs ON coordinates (lat,lon);
