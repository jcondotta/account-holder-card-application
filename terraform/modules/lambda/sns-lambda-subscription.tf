resource "aws_sns_topic_subscription" "this" {
  topic_arn = var.sns_bank_account_created_topic_arn
  protocol  = "lambda"
  endpoint  = aws_lambda_function.account_holder_card_application_lambda.arn

  depends_on = [
    aws_lambda_permission.allow_sns_invoke
  ]
}