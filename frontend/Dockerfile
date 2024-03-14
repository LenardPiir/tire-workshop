FROM node:alpine

WORKDIR /app

RUN npm install -g serve

COPY package.json /app

RUN npm install

COPY . /app

RUN npm run build

CMD serve -s build