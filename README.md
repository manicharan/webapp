# webapp

**Instructions to run the application**

1) Clone the code using git clone link/to/repository
2) Verify that you have the compatible versions of Java and MariaDB installed locally
3) cd into webapp-main
4) Run the application using the command ./mvnw spring-boot:run
5) Set AWS Profile -> using set AWS_PROFILE=demo
5) Import certificate using -> aws acm import-certificate --certificate fileb://path/to/your_certificate.crt --certificate-chain fileb://path/to/your_certificate.ca-bundle --private-key fileb://path/to/your_private_key.pem --region your_aws_region