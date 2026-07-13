FROM nginx:alpine
# 直接复制你本地已经打包完成的dist，跳过node编译所有步骤
COPY ./dist /usr/share/nginx/html
# 复制你的nginx配置
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]