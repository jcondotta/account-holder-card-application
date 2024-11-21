resource "aws_lambda_function" "account_holder_card_application_lambda" {
  function_name = var.lambda_function_name
  runtime       = var.lambda_runtime
  handler       = var.lambda_handler
  role          = aws_iam_role.account_holder_card_application_lambda_role_exec.arn
  filename      = var.lambda_file
  memory_size   = var.lambda_memory_size
  timeout       = var.lambda_timeout
  architectures = ["arm64"]

  environment {
    variables = merge(
      {
        AWS_SNS_BANK_ACCOUNT_CREATED_TOPIC_ARN = var.sns_bank_account_created_topic_arn
        AWS_SQS_CARD_APPLICATION_QUEUE_URL = var.sqs_card_application_queue_url
      },
      var.lambda_environment_variables
    )
  }

  tags = var.tags
}