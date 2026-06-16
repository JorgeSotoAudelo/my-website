import json
import base64
import logging
import os

import boto3
from botocore.exceptions import ClientError

logger = logging.getLogger()
logger.setLevel(logging.INFO)

s3_client = boto3.client("s3")

# Configuration
S3_BUCKET = os.environ.get("RESUME_BUCKET")
S3_KEY = os.environ.get("RESUME_S3_KEY")
FILENAME = os.environ.get("RESUME_FILENAME")


def lambda_handler(event, context):
    """
    Downloads a resume file from S3 and returns it as a binary response
    suitable for API Gateway (HTTP API or REST API with binary support).
    """
    try:
        return _inline_response()

    except ClientError as e:
        error_code = e.response["Error"]["Code"]
        logger.error("S3 error [%s]: %s", error_code, e)

        if error_code == "NoSuchKey":
            return _error(404, "Resume file not found.")
        if error_code in ("AccessDenied", "403"):
            return _error(403, "Access denied to resume file.")
        return _error(500, "Failed to retrieve resume.")

    except Exception as e:
        logger.exception("Unexpected error: %s", e)
        return _error(500, "An unexpected error occurred.")


# ---------------------------------------------------------------------------
# Private helpers
# ---------------------------------------------------------------------------

def _inline_response():
    """Fetch the file from S3 and embed it base64-encoded in the response body."""
    response = s3_client.get_object(Bucket=S3_BUCKET, Key=S3_KEY)
    file_bytes = response["Body"].read()

    logger.info(
        "Streaming resume inline: %s bytes", len(file_bytes)
    )
    return {
        "statusCode": 200,
        "headers": {
            "Content-Type": "application/pdf",
            "Content-Disposition": f'attachment; filename="{FILENAME}"',
            "Cache-Control": "no-store",
            "Accept": "application/pdf"
        },
        "body": base64.b64encode(file_bytes).decode("utf-8"),
        "isBase64Encoded": True,
    }


def _error(status_code: int, message: str) -> dict:
    return {
        "statusCode": status_code,
        "headers": {"Content-Type": "application/json"},
        "body": json.dumps({"error": message}),
    }