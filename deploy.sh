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

# app.jar 파일이 있는지 확인
if [ -e app.jar ]; then

    # app.jar 파일이 있다면 해쉬값 계산
    hash_value=$(md5sum app.jar | awk '{ print $1 }')

    # backup 디렉토리 생성 (이미 있다면 무시)
    mkdir -p backup

    # 백업 파일 경로 설정
    backup_file="backup/app-${hash_value}.jar"

    # 백업 실행
    cp app.jar "$backup_file"

    # backup 디렉토리에서 최신순으로 정렬 후 5개 파일만 유지
    ls -t backup/*.jar | tail -n +6 | xargs rm -f
else
    echo "app.jar 파일이 현재 디렉토리에 존재하지 않습니다."
fi

cp /home/sp/source/auth/target/*.jar app.jar

# formdang-sp-was 서비스를 시작
systemctl start formdang-sp-auth
echo "formdang-sp-was 서비스를 시작했습니다."