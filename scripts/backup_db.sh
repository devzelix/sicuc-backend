#!/bin/bash
set -euo pipefail
# ============================================================
# SICUC Database Backup Script (Production Ready)
# ------------------------------------------------------------
# Creates a full SQL dump of the MySQL database, compresses it,
# keeps logs, removes old backups, and can notify via email on failure.
#
# Usage: configure cron or run manually
#   chmod +x /usr/local/bin/backup_db.sh
#   /usr/local/bin/backup_db.sh
#
# Author: devzelix
# Last Updated: 2025-11-09
# ============================================================

# --- Load environment variables from .env (if exists) -----
if [ -f /app/.env ]; then
    echo "📦 Loading environment variables from .env..."
    set -a
    source /app/.env
    set +a
fi

# --- Configuration Variables -----------------------------
CONTAINER_NAME="${MYSQL_CONTAINER_NAME:-sicuc-db}"
DATABASE_NAME="${MYSQL_DATABASE:-sicuc_db}"
ROOT_PASSWORD="${MYSQL_ROOT_PASSWORD_SECRET:-YourStrongRootPassword}"
BACKUP_DIR="${BACKUP_DIR:-/backups}"
LOG_DIR="${LOG_DIR:-/var/log/sicuc_backups}"
RETENTION_DAYS="${RETENTION_DAYS:-7}"
EMAIL_ON_ERROR="${EMAIL_ON_ERROR:-}" # leave empty if not using email notifications

# Date formats
DATE_FULL=$(date +'%Y-%m-%d_%H-%M-%S')
DATE_SHORT=$(date +'%Y-%m-%d')
FILENAME="sicuc_backup_${DATE_FULL}.sql.gz"
LOG_FILE="${LOG_DIR}/backup_${DATE_SHORT}.log"

# Ensure directories exist
mkdir -p "${BACKUP_DIR}" "${LOG_DIR}"

# --- Start Logging ---------------------------------------
echo "🏁 Backup started at $(date)" | tee -a "${LOG_FILE}"

# --- Backup Execution -----------------------------------
{
    if [ -n "${MYSQL_CONTAINER_NAME:-}" ]; then
        # Running from host (Docker)
        echo "🔄 Dumping database '${DATABASE_NAME}' from container '${CONTAINER_NAME}'..."
        docker exec "${CONTAINER_NAME}" /usr/bin/mysqldump \
            -u root -p"${ROOT_PASSWORD}" \
            --databases "${DATABASE_NAME}" \
            --single-transaction \
            --quick \
            --lock-tables=false | gzip > "${BACKUP_DIR}/${FILENAME}"
    else
        # Running inside container
        echo "🔄 Dumping database '${DATABASE_NAME}' locally..."
        /usr/bin/mysqldump \
            -u root -p"${ROOT_PASSWORD}" \
            --databases "${DATABASE_NAME}" \
            --single-transaction \
            --quick \
            --lock-tables=false | gzip > "${BACKUP_DIR}/${FILENAME}"
    fi
} || {
    echo "❌ Backup failed at $(date)" | tee -a "${LOG_FILE}"
    # Optional email notification
    if [ -n "${EMAIL_ON_ERROR}" ]; then
        echo "Backup failed on $(hostname) at $(date)" | mail -s "SICUC DB Backup Failed" "${EMAIL_ON_ERROR}"
    fi
    exit 1
}

echo "✅ Backup completed successfully at $(date)" | tee -a "${LOG_FILE}"
echo "📦 File saved at: ${BACKUP_DIR}/${FILENAME}" | tee -a "${LOG_FILE}"

# --- Cleanup old backups ---------------------------------
echo "🧹 Cleaning up backups older than ${RETENTION_DAYS} days..." | tee -a "${LOG_FILE}"
find "${BACKUP_DIR}" -type f -name "*.sql.gz" -mtime +${RETENTION_DAYS} -exec rm {} \;
echo "🧹 Cleanup completed." | tee -a "${LOG_FILE}"

echo "🏁 Backup process finished at $(date)" | tee -a "${LOG_FILE}"
