#!/bin/bash

# formdang-sp-was 서비스의 상태를 확인
service_status=$(systemctl is-active formdang-sp-auth)

# formdang-sp-was 서비스가 실행 중인 경우
if [ "$service_status" == "active" ]; then
    # formdang-sp-was 서비스를 중지
    systemctl stop formdang-sp-auth
else
    echo "formdang-sp-auth 서비스는 이미 중지되어 있습니다."
fi

cd /home/sp/deploy/auth

cp /home/sp/source/auth/target/*.jar app.jar

sleep 3

# formdang-sp-was 서비스를 시작
systemctl start formdang-sp-auth
echo "formdang-sp-was 서비스를 시작했습니다."