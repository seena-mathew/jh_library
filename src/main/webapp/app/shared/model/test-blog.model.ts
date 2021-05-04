import dayjs from 'dayjs';

export interface ITestBlog {
  id?: number;
  title?: string;
  content?: string;
  createdOn?: string;
  updatedOn?: string;
  username?: string;
}

export const defaultValue: Readonly<ITestBlog> = {};
