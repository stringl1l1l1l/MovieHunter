import defaultSettings from '@/settings'

const title = '电影推荐系统管理端'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
