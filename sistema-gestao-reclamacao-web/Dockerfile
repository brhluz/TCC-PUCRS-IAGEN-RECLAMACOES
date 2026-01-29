# build
FROM node:16-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci || npm install
COPY . .
RUN npm run build

# runtime
FROM nginx:alpine
COPY --from=build /app/dist/sistema-gestao-reclamacao /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80