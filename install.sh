#!/usr/bin/env bash
set -o nounset
set -o pipefail

generate_jwt_secret() {
  echo "Preparing .env file in the project root..."
  
  if [ ! -f .env ]; then
    if [ -f .env.example ]; then
      cp .env.example .env
      echo "Created .env file from .env.example"
    else
      touch .env
      echo "Warning: .env.example not found. Creating an empty .env file."
    fi
  fi

  echo "Generating secure JWT secret..."
  local rand_secret
  if command -v openssl >/dev/null; then
    rand_secret=$(openssl rand -base64 32)
  else
    rand_secret=$(head -c 32 /dev/urandom | base64 | head -c 44)
  fi

  grep -v "^JWT_SECRET=" .env > .env.tmp 2>/dev/null || true
  mv .env.tmp .env
  echo "JWT_SECRET=${rand_secret}" >> .env
  
  echo "JWT secret successfully configured in your .env file."
}

start_docker_compose() {
  echo "Starting Docker containers (Java API + Next.js Frontend)..."

  if ! command -v docker >/dev/null || ! docker compose >/dev/null 2>&1; then
    echo "Error: 'docker compose' command not found. Please ensure Docker is installed and running."
    return 1
  fi

  if ! docker compose up --build --remove-orphans; then
    echo "Error: Could not start containers."
    return 1
  fi
  
  show_friendly_message
}

show_friendly_message() {
  cat <<EOF
-------------------------------------------------------
SafeNews Successfully Initialized
-------------------------------------------------------
The environment has been configured successfully.

Local access URLs:
- Frontend (Next.js): http://localhost:3000
- API (Spring Boot): http://localhost:8080

The database is currently empty. Please access the 
application via your browser to complete the first-time 
registration (Owner/Admin account setup).
-------------------------------------------------------
EOF
}

# MAIN EXECUTION
main() {
  echo "=== Starting SafeNews Setup ==="
  generate_jwt_secret
  start_docker_compose
}

main