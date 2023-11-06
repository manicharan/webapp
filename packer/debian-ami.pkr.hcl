packer {
  required_plugins {
    amazon = {
      source  = "github.com/hashicorp/amazon"
      version = ">= 1.0.0"
    }
  }
}

variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "source_ami" {
  type    = string
  default = "ami-06db4d78cb1d3bbf9" # Debian 12
}

variable "ssh_username" {
  type    = string
  default = "admin"
}

variable "subnet_id" {
  type    = string
  default = "subnet-0197bbb7b45affbe2"
}

# https://www.packer.io/plugins/builders/amazon/ebs
source "amazon-ebs" "my-ami" {
  region          = "${var.aws_region}"
  ami_name        = "csye6225_${formatdate("YYYY_MM_DD_hh_mm_ss", timestamp())}"
  ami_description = "AMI for CSYE 6225"
  ami_users       = ["807352099833"]
  ami_regions = [
    "us-east-1",
  ]

  aws_polling {
    delay_seconds = 120
    max_attempts  = 50
  }

  instance_type = "t2.micro"
  source_ami    = "${var.source_ami}"
  ssh_username  = "${var.ssh_username}"
  subnet_id     = "${var.subnet_id}"

  launch_block_device_mappings {
    delete_on_termination = true
    device_name           = "/dev/xvda"
    volume_size           = 8
    volume_type           = "gp2"
  }
}

build {
  sources = ["source.amazon-ebs.my-ami"]

  provisioner "file" {
    source      = "target/webapp-0.0.1-SNAPSHOT.jar"
    destination = "/tmp/webapp-0.0.1-SNAPSHOT.jar"
  }

  provisioner "file" {
    source      = "packer/users.csv"
    destination = "/tmp/users.csv"
  }

  provisioner "file" {
    source      = "packer/webservice.service"
    destination = "/tmp/webservice.service"
  }

  provisioner "file" {
    source      = "packer/cloudwatch-config.json"
    destination = "/tmp/cloudwatch-config.json"
  }

  provisioner "shell" {
    environment_vars = [
      "DEBIAN_FRONTEND=noninteractive",
      "CHECKPOINT_DISABLE=1"
    ]
    inline = [
      "sudo groupadd csye6225",
      "sudo useradd -s /bin/false -g csye6225 -d /opt/csye6225 -m csye6225",
      "sudo apt-get update",
      "sudo apt-get upgrade -y",
      "sudo apt-get clean",
      "sudo apt-get install -y openjdk-17-jre",
      "pwd",
      "ls -al",
      "sudo mv /tmp/webapp-0.0.1-SNAPSHOT.jar /opt/csye6225",
      "sudo mv /tmp/users.csv /opt",
      "sudo mv /tmp/cloudwatch-config.json /opt",
      "sudo cp /tmp/webservice.service /etc/systemd/system",
      "sudo touch /opt/csye6225/application.properties",
      "sudo chown csye6225:csye6225 /opt/csye6225/application.properties",
      "sudo chown csye6225:csye6225 /opt/csye6225/webapp-0.0.1-SNAPSHOT.jar",
#      "sudo chown csye6225:csye6225 /etc/systemd/system/webservice.service",
      "sudo chmod 750 /opt/csye6225/webapp-0.0.1-SNAPSHOT.jar",
      "sudo chmod 750 /opt/csye6225/application.properties",
      "wget https://amazoncloudwatch-agent.s3.amazonaws.com/debian/amd64/latest/amazon-cloudwatch-agent.deb",
      "sudo dpkg -i -E ./amazon-cloudwatch-agent.deb",
      "sudo systemctl daemon-reload",
      "sudo systemctl start webservice",
      "sudo systemctl enable webservice",
      #      "sudo systemctl restart webservice",
      #      "sudo systemctl stop webservice",
      "echo \"inside opt\"",
      "cd ~/../../opt",
      "ls -al",
      "cd csye6225",
      "pwd",
      "ls -al"
    ]
  }
}