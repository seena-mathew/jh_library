import dayjs from 'dayjs';

export interface ISearch {
  id?: number;
  title?: string;
  content?: string;
  createdOn?: string;
  updatedOn?: string;
  username?: string;
}

export const defaultValue: Readonly<ISearch> = {};
